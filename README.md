[Install Docker Desktop](https://docs.docker.com/desktop/) for your platform.

```
$ docker build -t software-engineer-project-template .

# A bunch of Docker output goes here
# Hopefully ending in success

$ docker run --volume "$(pwd)"/input:/input --volume "$(pwd)"/output:/output -it --rm software-engineer-project-template /input/input-2.csv /output/output-2.json
Hello world!
Reading input from /input/input-2.csv
Parse Penalties for 3608 plays
Writing output to /output/output-2.json
Done
```
