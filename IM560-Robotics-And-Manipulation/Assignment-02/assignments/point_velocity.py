import numpy as np

OMEGA_S = np.array([0, 0, 3])
X_CM = np.array([1, 2, 0])
V_CM = np.array([2, -1, 0])
X_P = np.array([3, 2, 0])
X_Q = np.array([1, 4, 2])

def main():
    v_p = V_CM + np.cross(OMEGA_S, (X_P - X_CM))
    print(f"v_p: {v_p}")
    print(f"Velocity of P: {np.linalg.norm(v_p)}")
    v_q = V_CM + np.cross(OMEGA_S, (X_Q - X_CM))
    print(f"v_q: {v_q}")
    print(f"Velocity of Q: {np.linalg.norm(v_q)}")


if __name__ == "__main__":
    main()
