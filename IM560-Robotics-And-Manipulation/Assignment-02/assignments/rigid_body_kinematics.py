import numpy as np

L = 2
X_CM = np.array([0, 0, 0])
V_CM = np.array([1, 0, 0])
R = np.eye(3)
OMEGA_B = np.array([0, 0, 2])
OMEGA_B_X = np.array([
    [0, -2, 0],
    [2,  0, 0],
    [0,  0, 0],
])

def rotation_matrix(t):
    t = np.linalg.norm(OMEGA_B) * t
    return np.array([
        [np.cos(t), -np.sin(t), 0],
        [np.sin(t), np.cos(t), 0],
        [0, 0, 1]
    ])

def x_cm_at_t(t):
    return X_CM + V_CM * t

def main():
    r1 = np.array([-1, 0, 0])
    r2 = np.array([1, 0, 0])

    x_p1 = X_CM + R @ r1
    x_p2 = X_CM + R @ r2

    print(f"x_p1 = {x_p1}")
    print(f"x_p2 = {x_p2}")

    t = np.pi / 4.0

    R_t = rotation_matrix(t)

    x_p1 = x_cm_at_t(t) + R_t @ r1
    x_p2 = x_cm_at_t(t) + R_t @ r2

    print(f"x_p1 after pi / 4 seconds = {x_p1}")
    print(f"x_p2 after pi / 4 seconds = {x_p2}")

    omega_s = R_t @ OMEGA_B
    v_p1 = V_CM + np.cross(omega_s, (x_p1 - X_CM))
    v_p2 = V_CM + np.cross(omega_s, (x_p2 - X_CM))
    print(f"v_p1 = {v_p1}")
    print(f"v_p2 = {v_p2}")


if __name__ == "__main__":
    main()