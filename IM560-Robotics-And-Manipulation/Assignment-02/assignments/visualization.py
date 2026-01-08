import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.animation import FuncAnimation

L, W, H = 2.0, 1.0, 0.5
omega_b = np.array([1.0, 2.0, 0.5])
v_cm = np.array([0.5, 0.5, 0.0])
x_cm = np.array([0.0, 0.0, 0.0])
R = np.eye(3)

r_body = np.array([
    [ +L/2, +W/2, +H/2 ],
    [ +L/2, +W/2, -H/2 ],
    [ +L/2, -W/2, +H/2 ],
    [ +L/2, -W/2, -H/2 ],
    [ -L/2, +W/2, +H/2 ],
    [ -L/2, +W/2, -H/2 ],
    [ -L/2, -W/2, +H/2 ],
    [ -L/2, -W/2, -H/2 ],
])

def update_rigid_body_state(x_cm, v_cm, R, omega_s, dt):
    x_cm_new = x_cm + v_cm * dt
    omega_s_x = np.array([
        [         0,      -omega_s[2],  omega_s[1]],
        [ omega_s[2],              0, -omega_s[0]],
        [-omega_s[1],       omega_s[0],         0]
    ])
    R_dot = omega_s_x @ R
    R_new = R + R_dot * dt
    return x_cm_new, R_new

def check_frobenius_norm(R):
    error = np.linalg.norm(R.T @ R - np.eye(3), 'fro')
    return error

def orthonormalize(R):
    U, _, Vt = np.linalg.svd(R)
    R_orth = U @ Vt
    if np.linalg.det(R_orth) < 0:
        U[:, -1] *= -1
        R_orth = U @ Vt
    return R_orth

def main():
    dt = 0.01
    sim_time = 10.0  # seconds
    total_frames = int(sim_time / dt)

    x_p = x_cm + r_body @ R.T   # row-vectors -> multiply by R.T

    fig = plt.figure(figsize=(10, 10))
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlim(-2, 2)
    ax.set_ylim(-2, 2)
    ax.set_zlim(-2, 2)

    edges = np.array([
        [0, 1], [0, 2], [0, 4],
        [1, 3], [1, 5],
        [2, 3], [2, 6],
        [3, 7],
        [4, 5], [4, 6],
        [5, 7],
        [6, 7],
    ])

    box_lines = []
    for e in edges:
        x1, y1, z1 = x_p[e[0]]
        x2, y2, z2 = x_p[e[1]]
        line, = ax.plot3D([x1, x2], [y1, y2], [z1, z2], color="red")
        box_lines.append(line)

    axis_lines = []
    for c in ["r", "g", "b"]:
        line, = ax.plot3D([], [], [], color=c)
        axis_lines.append(line)

    state = {
        "x_cm": x_cm.copy(),
        "R": R.copy(),
    }

    def update(frame):
        omega_s = state["R"] @ omega_b

        state["x_cm"], state["R"] = update_rigid_body_state(
            state["x_cm"], v_cm, state["R"], omega_s, dt
        )
        state["R"] = orthonormalize(state["R"])

        error = check_frobenius_norm(state["R"])
        if(error != 0):
            state["R"] = orthonormalize(state["R"])

        x_p_new = state["x_cm"] + r_body @ state["R"].T

        for line, e in zip(box_lines, edges):
            x1, y1, z1 = x_p_new[e[0]]
            x2, y2, z2 = x_p_new[e[1]]
            line.set_data([x1, x2], [y1, y2])
            line.set_3d_properties([z1, z2])

        origin = state["x_cm"]
        x_axis = state["R"] @ np.array([1.0, 0.0, 0.0])
        y_axis = state["R"] @ np.array([0.0, 1.0, 0.0])
        z_axis = state["R"] @ np.array([0.0, 0.0, 1.0])

        axes_world = [x_axis, y_axis, z_axis]

        for line, a in zip(axis_lines, axes_world):
            line.set_data(
                [origin[0], origin[0] + a[0]],
                [origin[1], origin[1] + a[1]],
            )
            line.set_3d_properties(
                [origin[2], origin[2] + a[2]]
            )

        return box_lines + axis_lines

    ani = FuncAnimation(fig, update, frames=total_frames, interval=50, blit=False)
    plt.show()

if __name__ == "__main__":
    main()
