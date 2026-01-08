Ignore references to the SAC algorithm!

## Experiments:

### Basic Q-Learning

```bash
# CartPole-v1:
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/cartpole.yaml

# LunarLander-v2
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/lunarlander.yaml --seed 1 
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/lunarlander.yaml --seed 2
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/lunarlander.yaml --seed 3
```

### Double-Q-Learning

```bash
# LunarLander-v2
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/lunarlander_doubleq.yaml --seed 1
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/lunarlander_doubleq.yaml --seed 2
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/lunarlander_doubleq.yaml --seed 3

# MsPacman-v0
python cs285/scripts/run_hw3_dqn.py -cfg experiments/dqn/mspacman.yaml
```