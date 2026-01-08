import numpy as np
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

def area_of_polygon(points):
    x = points[0, :]
    y = points[1, :]
    
    # Shoelace formula
    area = 0.5 * np.abs(np.dot(x, np.roll(y, -1)) - np.dot(y, np.roll(x, -1)))

    print("Area of polygon:", area)

def rotate(points, theta):
    R = np.array([
        [np.cos(theta), -np.sin(theta)],
        [np.sin(theta),  np.cos(theta)]
    ])
    return R @ points

def main():
    theta_final = np.pi / 4  # 45 degrees
    total_frames = 100 # for animation

    points = np.array([
        [-1,  1,  1, -1, -1],
        [-1, -1,  1,  1, -1]
    ])

    rotated_points = rotate(points, theta_final)

    area_of_polygon(points)
    area_of_polygon(rotated_points)

    # Calculate area after rotating by matrix A
    A = np.array([
        [2, 1], 
        [8, 4]
    ]) 
    points_rotated_by_A = A @ points 
    area_of_polygon(points_rotated_by_A)

    # Set up plot
    fig, ax = plt.subplots()
    ax.set_xlim(-2, 2)
    ax.set_ylim(-2, 2)
    ax.set_aspect('equal')
    ax.grid(True)

    # Plot initial polygon
    line, = ax.plot(points[0], points[1], 'b-', linewidth=2)    

    # Update function for animation
    def update(frame):
        theta = (frame / total_frames) * theta_final
        rotated_points = rotate(points, theta)
        line.set_data(rotated_points[0], rotated_points[1])
        return line,

    # Create animation
    ani = FuncAnimation(fig, update, frames=total_frames, interval=50, blit=True)
    
    plt.show()

if __name__ == "__main__":
    main()
