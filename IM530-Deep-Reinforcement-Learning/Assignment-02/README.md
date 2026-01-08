## Setup

See [installation.md](installation.md). It's worth going through this again since some dependencies have changed since homework 1. You also need to make sure to run `pip install -e .` in this folder.

## Complete the code

There are TODOs in these files:

- `cs285/scripts/run_hw2.py`
- `cs285/agents/pg_agent.py`
- `cs285/networks/policies.py`
- `cs285/networks/critics.py`
- `cs285/infrastructure/utils.py`

See the [Assignment](https://moodle.haw-landshut.de/pluginfile.php/889533/mod_resource/content/1/rec02.html) for more details.

## Answer the following questions briefly:

### Which value estimator has better performance without advantage normalization: the trajectory-centric one, or the one using reward-to-go?
**A:** The one using reward-to-go

### Did advantage normalization help?
**A:** Yes

### Did the batch size make an impact?
**A:** Yes

### Provide the exact command line configurations (or #@params settings in Colab) you used to run your experiments, including any parameters changed from their defaults.
**A:** See below

## Scripts

### Experiment 1 (CartPole). Run multiple experiments with the PG algorithm on the discrete CartPole-v0 environment, using the following commands:

```bash
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 1000 \
--exp_name cartpole && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 1000 \
-rtg --exp_name cartpole_rtg && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 1000 \
-na --exp_name cartpole_na && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 1000 \
-rtg -na --exp_name cartpole_rtg_na && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 4000 \
--exp_name cartpole_lb && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 4000 \
-rtg --exp_name cartpole_lb_rtg && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 4000 \
-na --exp_name cartpole_lb_na && \
python cs285/scripts/run_hw2.py --env_name CartPole-v0 -n 100 -b 4000 \
-rtg -na --exp_name cartpole_lb_rtg_na
```

### Experiment 2 (HalfCheetah). Next, you will use your baselined policy gradient implementation to learn a controller for HalfCheetah-v4. Run the following commands:

```bash
# No baseline
python cs285/scripts/run_hw2.py --env_name HalfCheetah-v4 \
-n 100 -b 5000 -rtg --discount 0.95 -lr 0.01 \
--exp_name cheetah
# Baseline
python cs285/scripts/run_hw2.py --env_name HalfCheetah-v4 \
-n 100 -b 5000 -rtg --discount 0.95 -lr 0.01 \
--use_baseline -blr 0.01 -bgs 5 --exp_name cheetah_baseline
```

#### Run another experiment with a decreased number of baseline gradient steps (-bgs) and/or baseline learning rate (-blr). How does this affect (a) the baseline learning curve and (b) the performance of the policy?
**A:** (a) Takes longer to converge (b) Performance decreases

### Experiment 3 (LunarLander-v2). You will now use your implementation of policy gradient with generalized advantage estimation to learn a controller for a version of LunarLander-v2 with noisy actions. Search over λ∈[0,0.95,0.98,0.99,1] to replace <lambda> below. Do not change any of the other hyperparameters (e.g. batch size, learning rate). The run with the best performance should achieve an average score close to 200 (180+).

```bash
for lambda in 0 0.95 0.98 0.99 1; do
python cs285/scripts/run_hw2.py \
--env_name LunarLander-v2 --ep_len 1000 \
--discount 0.99 -n 300 -l 3 -s 128 -b 2000 -lr 0.001 \
--use_reward_to_go --use_baseline --gae_lambda $lambda \
--exp_name lunar_lander_lambda$lambda
done
```

#### Provide a single plot with the learning curves for the LunarLander-v2 experiments that you tried. Describe in words how λ affected task performance.
**A:** Not much? All experiments got similar AverageReturn in the end. But the loss has way less variance with lower λ

#### Consider the parameter λ. What does λ=0 correspond to? What about λ=1? Relate this to the task performance in LunarLander-v2 in one or two sentences.
**A:** λ=0: high bias, low variance (Temporal Difference)
λ=1: low bias, high variance (Monte Carlo)

### Experiment 4 (InvertedPendulum). First, train a policy for the inverted pendulum problem without GAE and with otherwise default settings. Use five different seeds to get a good estimate of average performance:

```bash
for seed in $(seq 1 5); do
python cs285/scripts/run_hw2.py --env_name InvertedPendulum-v4 -n 100 \
--exp_name pendulum_default_s$seed \
-rtg --use_baseline -na \
--batch_size 5000 \
--seed $seed
done
```

Your task is to tune hyperparameters so that your implementation reaches maximum performance (score of 1000) in fewer environment steps than these default settings (note: this is not the same as minimizing number of policy gradient iterations: one policy gradient iteration corresponds to batch size environment steps). Try and find hyperparameters that reach a score of 1000 in as few steps as possible!

```bash
python cs285/scripts/run_hw2.py --env_name InvertedPendulum-v4 -n 100 \
--exp_name pendulum_best \
-rtg --use_baseline -na \
--batch_size 5000 \
--discount 0.99 --gae_lambda 0.95 -l 3 -s 128
```