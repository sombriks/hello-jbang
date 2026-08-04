# Onstalling JBang

Several options:

## SDKMan!

First, [install sdkman](https://sdkman.io/)

```bash
sdk install jbang
```

## asdf

First, [install asdf](https://asdf-vm.com/guide/getting-started.html)

```bash
asdf plugin-add jbang
asdf install jbang latest
asdf global jbang latest
```

You can also check a list of alternative methods to get it in the
[project site][s].

[s]: https://www.jbang.dev/documentation/jbang/latest/installation.html

## Bash completon

Add the following line at the end of your `.bashrc`:

```bash
source <(jbang completion)
```
