import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.animation import FuncAnimation

def update_rigid_body_state(x_cm, v_cm, R, omega_s, dt):
    """
    Update rigid body state using first-order Euler integration.
    
    Parameters:
    -----------
    x_cm : np.ndarray, shape (3,)
        Current center of mass position
    v_cm : np.ndarray, shape (3,)
        Current center of mass velocity (assumed constant)
    R : np.ndarray, shape (3, 3)
        Current rotation matrix
    omega_s : np.ndarray, shape (3,)
        Spatial angular velocity (assumed constant)
    dt : float
        Time step
        
    Returns:
    x_cm_new : np.ndarray, shape (3,)
        Updated center of mass position
    R_new : np.ndarray, shape (3, 3)
        Updated rotation matrix
    """

    x_cm_new = x_cm + v_cm * dt
    omega_s_x = np.array([
        [     0,      -omega_s[2],  omega_s[1]],
        [ omega_s[2],      0,      -omega_s[0]],
        [-omega_s[1],  omega_s[0],      0     ]
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

def plot_trajectory(x_arr, color='red'):
    fig = plt.figure(figsize=(10,10))
    ax = fig.add_subplot(111, projection='3d')
    ax.plot3D(x_arr[:,0], x_arr[:,1], x_arr[:,2], color=color, linewidth=2)
    ax.set_xlim(-10,10)
    ax.set_ylim(-10,10)
    ax.set_zlim(-10,10)
    ax.set_box_aspect([1,1,1])
    ax.grid(True)
    plt.show()


def main():
    x_cm = np.array([0, 0, 0])
    v_cm = np.array([1, 0, 0])
    R = np.eye(3)
    omega_s = np.array([0, 0, 1])
    dt = 0.01 # seconds
    r_body = np.array([1, 0, 0])
    x_p = x_cm + R @ r_body

    x_cm_positions = [x_cm]
    x_p_positions = [x_p]

    x_cm_new, R_new = x_cm, R
    for i in range(1000):
        x_cm_new, R_new = update_rigid_body_state(x_cm_new, v_cm, R_new, omega_s, dt)

        error = check_frobenius_norm(R_new)
        if(error != 0):
            R_new = orthonormalize(R_new)

        x_p_new = x_cm_new + R_new @ r_body

        x_cm_positions.append(x_cm_new)
        x_p_positions.append(x_p_new)

    x_cm_new_arr = np.array(x_cm_positions)
    x_p_new_arr = np.array(x_p_positions)

    plot_trajectory(x_cm_new_arr)
    plot_trajectory(x_p_new_arr)


if __name__ == "__main__":
    main()