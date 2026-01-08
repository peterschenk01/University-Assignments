import numpy as np
import torch
from torch import Tensor
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

PLANAR_3R_TARGET = torch.tensor([-5.0, -7.0], dtype=torch.float32)
RRPRP_TARGET = torch.tensor([-20.0, -17.0], dtype=torch.float32)

def transformation_matrix_revolute(a, theta):
    return torch.stack([
        torch.stack([torch.cos(theta), -torch.sin(theta), a * torch.cos(theta)]),
        torch.stack([torch.sin(theta),  torch.cos(theta), a * torch.sin(theta)]),
        torch.tensor([0.0, 0.0, 1.0])
    ])

def transformation_matrix_prismatic(d):
    return torch.stack([
        torch.stack([torch.tensor(1.0), torch.tensor(0.0),  d ]),
        torch.tensor([0.0, 1.0, 0.0]),
        torch.tensor([0.0, 0.0, 1.0])
    ])

def forward_kinematics_3r_planar(q: Tensor):
    l1, l2, l3 = 10.0, 8.0, 4.0
    t1 = transformation_matrix_revolute(l1, q[0])
    t2 = transformation_matrix_revolute(l2, q[1])
    t3 = transformation_matrix_revolute(l3, q[2])

    t1 = t1
    t2 = t1 @ t2
    t3 = t2 @ t3

    x0 = torch.tensor([0, 0, 1], dtype=torch.float32)
    x1 = t1 @ x0
    x2 = t2 @ x0
    end_effector = t3 @ x0

    joint_positions = torch.stack([x0[:-1], x1[:-1], x2[:-1], end_effector[:-1]])

    return joint_positions

def forward_kinematics_rrprp(q: Tensor):
    l1, l2, l3, l4, l5 = 10.0, 8.0, 4.0, 6.0, 2.0
    t1 = transformation_matrix_revolute(l1, q[0])
    t2 = transformation_matrix_revolute(l2, q[1])
    t3 = transformation_matrix_prismatic(q[2] + l3)
    t4 = transformation_matrix_revolute(l4, q[3])
    t5 = transformation_matrix_prismatic(q[4] + l5)

    t1 = t1
    t2 = t1 @ t2
    t3 = t2 @ t3
    t4 = t3 @ t4
    t5 = t4 @ t5

    x0 = torch.tensor([0, 0, 1], dtype=torch.float32)
    x1 = t1 @ x0
    x2 = t2 @ x0
    x3 = t3 @ x0
    x4 = t4 @ x0
    end_effector = t5 @ x0

    joint_positions = torch.stack([x0[:-1], x1[:-1], x2[:-1], x3[:-1], x4[:-1], end_effector[:-1]])

    return joint_positions

def end_effector_pose(q: Tensor):
    if(q.numel() == 3):
        joint_positions = forward_kinematics_3r_planar(q)
        x = joint_positions[-1]
    elif(q.numel() == 5):
        joint_positions = forward_kinematics_rrprp(q)
        x = joint_positions[-1]
    else:
        return None
    return x

def iterative_ik(q0, x_target, max_iter=10000, tolerance=0.005, damping=0.0001):
    q = q0
    for i in range(max_iter):
        x_current = end_effector_pose(q)
        error = x_target - x_current
        error_norm = torch.norm(error).item()

        if (error_norm < tolerance):
            return q
        
        J = torch.autograd.functional.jacobian(end_effector_pose, q)

        # J_inv = torch.linalg.pinv(J)
        JJT = J @ J.T
        damping = damping * torch.eye(JJT.shape[0])
        J_inv = J.T @ torch.linalg.inv(JJT + damping) # Singularity robustness

        delta_q = J_inv @ error
        q = q + delta_q
    
    print("Failed to converge")
    return None

def plot(configurations):
    plt.figure(figsize=(10,10))

    colors = ["tab:blue", "tab:green"]

    for i, joint_positions in enumerate(configurations):
        xs = [p[0] for p in joint_positions]
        ys = [p[1] for p in joint_positions]

        plt.plot(xs, ys, '-o', linewidth=3, markersize=8, color=colors[i])

    plt.axis("equal")
    plt.grid(True)
    plt.show()

def animate_motion_profile(q: Tensor, q_star: Tensor):
    q, q_star = q.detach(), q_star.detach()

    fig, ax = plt.subplots(figsize=(10,10))
    ax.set_xlim(-30, 30)
    ax.set_ylim(-30, 30)
    ax.set_aspect('equal')
    ax.grid(True)

    if(q.numel() == 3):
        initial_positions = forward_kinematics_3r_planar(q)
    elif(q.numel() == 5):
        initial_positions = forward_kinematics_rrprp(q)
    else:
        initial_positions = torch.tensor([])
    initial_positions = initial_positions.detach().numpy()
    
    xs = [p[0] for p in initial_positions]
    ys = [p[1] for p in initial_positions]

    line, = ax.plot(xs, ys, '-o', linewidth=3, markersize=8, color="tab:blue")

    total_frames = 100
    def update(frame):
        tT = frame / total_frames
        # MP = tT
        MP = 0.5 * (1 - np.cos(np.pi * tT))
        qt: Tensor = q + MP * (q_star - q)

        if(qt.numel() == 3):
            joint_positions = forward_kinematics_3r_planar(qt)
        elif(qt.numel() == 5):
            joint_positions = forward_kinematics_rrprp(qt)
        else:
            joint_positions = torch.tensor([])
        joint_positions = joint_positions.detach().numpy()

        xs = [p[0] for p in joint_positions]
        ys = [p[1] for p in joint_positions]

        line.set_data(xs, ys)
        return line,

    ani = FuncAnimation(fig, update, frames=total_frames, interval=50, blit=True)
    plt.show()

def planar_3r():
    # 3R planar before
    q = torch.tensor(np.radians([30, 45, -60]), dtype=torch.float32, requires_grad=True)
    initial_positions = forward_kinematics_3r_planar(q)
    initial_positions = initial_positions.detach().numpy()

    # IK target
    q_star = iterative_ik(q, PLANAR_3R_TARGET)
    if q_star is None: 
        return

    # 3R planar after
    final_positions = forward_kinematics_3r_planar(q_star)
    final_positions = final_positions.detach().numpy()

    plot([initial_positions, final_positions])
    animate_motion_profile(q, q_star)

def rrprp():
    # RRPRP before
    q = torch.tensor([np.radians(30), np.radians(45), 8.0, np.radians(-60), 4.0], dtype=torch.float32, requires_grad=True)
    initial_positions = forward_kinematics_rrprp(q)
    initial_positions = initial_positions.detach().numpy()

    # IK target
    q_star = iterative_ik(q, RRPRP_TARGET)
    if q_star is None: 
        return

    # RRPRP after
    final_positions = forward_kinematics_rrprp(q_star)
    final_positions = final_positions.detach().numpy()

    plot([initial_positions, final_positions])
    animate_motion_profile(q, q_star)

def main():
    planar_3r()
    rrprp()



if __name__ == "__main__":
    main()
