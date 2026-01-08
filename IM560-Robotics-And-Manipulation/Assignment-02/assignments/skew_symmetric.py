import numpy as np

def main():
    v = np.array([2, -1, 3])
    v_x = np.array([
    [0, -v[2], v[1]],
    [v[2], 0, -v[0]],
    [-v[1], v[0], 0]
    ])
    v_x_2 = np.outer(v, v.T) - ((np.linalg.norm(v) ** 2) * np.eye(3))
    v_x_3 = -(np.linalg.norm(v) ** 2) * v_x

    print(f"v_x = \n{v_x}")
    print(f"v_x * v = {v_x @ v}")
    print(f"v_x² = \n{v_x_2}")
    print(f"v_x² with numpy = \n{np.linalg.matrix_power(v_x, 2)}")
    print(f"v_x³ = \n{v_x_3}")
    print(f"v_x³ with numpy = \n{np.linalg.matrix_power(v_x, 3)}")




if __name__ == "__main__":
    main()