import numpy as np
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt

def r_x(theta):
    return np.array([
        [1,        0,              0     ],
        [0, np.cos(theta), -np.sin(theta)],
        [0, np.sin(theta),  np.cos(theta)]
    ])

def r_y(theta):
    return np.array([
        [ np.cos(theta), 0, np.sin(theta)],
        [       0,       1,       0      ],
        [-np.sin(theta), 0, np.cos(theta)]
    ])

def r_z(theta):
    return np.array([
        [np.cos(theta), -np.sin(theta), 0],
        [np.sin(theta),  np.cos(theta), 0],
        [     0,              0,        1]
    ])


def main():
    theta = np.radians(45)
    R_x = r_x(theta)
    R_y = r_y(theta)
    R_z = r_z(theta)
    I = np.identity(3)

    print(f"Show that (R_x @ R_y) != (R_y @ R_x) \n{(R_x @ R_y) == (R_y @ R_x)}\n")

    print(f"Verify orthogonality (R @ R_T = I):")
    print(f"For R_x:\n{np.round(R_x @ R_x.T)}")
    print(f"For R_y:\n{np.round(R_y @ R_y.T)}")
    print(f"For R_z:\n{np.round(R_z @ R_z.T)}")

if __name__ == "__main__":
    main()