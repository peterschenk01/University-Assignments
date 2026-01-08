import numpy as np

R = np.array([
    [ 0.6, 0, 0.8],
    [  0,  1,  0 ],
    [-0.8, 0, 0.6]
])

R_DOT = np.array([
    [-1.6, 0,  1.2],
    [  0,  0,   0 ],
    [-1.2, 0, -1.6]
])

def extract_vector_from_skew_symmetric_matrix(matrix):
    vector = np.array([
        matrix[2][1], #A32
        matrix[0][2], #A13
        matrix[1][0], #A21
    ])
    return vector

def spatial_angular_velocity():
    omega_s_x = R_DOT @ R.T
    omega_s = extract_vector_from_skew_symmetric_matrix(omega_s_x)
    return omega_s

def body_angular_velocity():
    omega_b_x = R.T @ R_DOT
    omega_b = extract_vector_from_skew_symmetric_matrix(omega_b_x)
    return omega_b

def main():
    omega_s = spatial_angular_velocity()
    print(f"omega_s: {omega_s}")
    omega_b = body_angular_velocity()
    print(f"omega_b: {omega_b}")
    print(f"Verify that omega_s = R * omega_b: {omega_s == R @ omega_b}")
    print("omega_s is rotating around the Y axis")

if __name__ == "__main__":
    main()
