import numpy as np
import matplotlib.pyplot as plt

def transformation_matrix(a, theta):
    return np.array([
        [np.cos(theta), -np.sin(theta), a * np.cos(theta)],
        [np.sin(theta),  np.cos(theta), a * np.sin(theta)],
        [0, 0, 1],
    ])

def forward_kinematics(q):
    l1, l2, l3 = 10.0, 8.0, 4.0
    t1 = transformation_matrix(l1, q[0])
    t2 = transformation_matrix(l2, q[1])
    t3 = transformation_matrix(l3, q[2])

    # Transformation of each joint in base frame
    T1 = t1
    T2 = t1 @ t2
    T3 = T2 @ t3

    # Compute joint positions
    x0 = np.array([0, 0, 1])
    x1 = T1 @ np.array([0, 0, 1])
    x2 = T2 @ np.array([0, 0, 1])
    end_effector = T3 @ np.array([0, 0, 1])

    joint_positions = np.array([
        [x0[0], x0[1]],
        [x1[0], x1[1]],
        [x2[0], x2[1]],
        [end_effector[0], end_effector[1]],
    ])

    return joint_positions

def plot_robot(joint_positions):
    # Extract x, y coordinates of joints
    xs = [p[0] for p in joint_positions]
    ys = [p[1] for p in joint_positions]

    plt.figure(figsize=(6,6))
    plt.plot(xs, ys, '-o', linewidth=3, markersize=8, color='tab:blue')
    plt.scatter(xs[-1], ys[-1], color='red', s=80, label='End Effector')

    plt.title("3R Planar Robot Visualization")
    plt.xlabel("X-axis")
    plt.ylabel("Y-axis")
    plt.axis("equal")
    plt.grid(True)
    plt.legend()
    plt.show()

def main():
    q = np.array(np.radians([30, 45, -60]))

    joint_positions = forward_kinematics(q)
    print("End-effector position: ", np.round(joint_positions[-1], 3))

    plot_robot(joint_positions)


if __name__ == "__main__":
    main()
