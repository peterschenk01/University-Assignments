import numpy as np
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt

def main():
    fig, ax = plt.subplots()
    ax.set_xlim(-5, 5)
    ax.set_ylim(-5, 5)
    ax.grid(True)
    ax.set_aspect('equal')

    v = np.array([3, 2])
    origin = np.array([0, 0])
    theta = np.radians(45)

    frame_a = np.array([
        [1, 0],
        [0, 1]
    ])

    frame_b = np.array([
        [ np.cos(theta), np.sin(theta)],
        [-np.sin(theta), np.cos(theta)]
    ])

    v_a = frame_a @ v
    v_b = frame_b @ v

    norm_v_a = np.linalg.norm(v_a)
    norm_v_b = np.linalg.norm(v_b)

    print(f"v_a: {v_a}")
    print(f"v_b: {v_b}")
    print(f"Norm of v_a: {norm_v_a}")
    print(f"Norm of v_b: {norm_v_b}")

    # Vector
    ax.quiver(*origin, *v_a, angles='xy', scale_units='xy', scale=1, color='g', label="V")

    # frames
    ax.quiver(*origin, *(100* frame_a[0]), angles='xy', scale_units='xy', scale=1, color='black', label="Frame A")
    ax.quiver(*origin, *(100* frame_a[1]), angles='xy', scale_units='xy', scale=1, color='black')

    ax.quiver(*origin, *(100* frame_b[0]), angles='xy', scale_units='xy', scale=1, color='grey', label="Frame B")
    ax.quiver(*origin, *(100* frame_b[1]), angles='xy', scale_units='xy', scale=1, color='grey')

    ax.legend()
    plt.show()


if __name__ == "__main__":
    main()