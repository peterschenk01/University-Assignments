import sympy as sp
from sympy import symbols, cos, sin, init_printing
import numpy as np

def r_x(theta):
    return sp.Matrix([
        [1,      0,           0     ],
        [0,  cos(theta), -sin(theta)],
        [0,  sin(theta),  cos(theta)]
    ])

def r_y(theta):
    return sp.Matrix([
        [ cos(theta), 0, sin(theta)],
        [     0,      1,     0     ],
        [-sin(theta), 0, cos(theta)]
    ])

def r_z(theta):
    return sp.Matrix([
        [cos(theta), -sin(theta), 0],
        [sin(theta),  cos(theta), 0],
        [    0,           0,      1]
    ])

def r(alpha, beta, gamma):
    return r_z(alpha) * r_y(beta) * r_x(gamma)

def main():
    init_printing()
    alpha, beta, gamma = symbols('alpha beta gamma')

    R = r(alpha, beta, gamma)

    print("Full rotation matrix:")
    sp.pprint(R)

    print("\nWith beta = 90°:")
    beta = sp.pi / 2
    R = r(alpha, beta, gamma)
    sp.pprint(R)


if __name__ == "__main__":
    main()
