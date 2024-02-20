# Java Installation Guide

This documentation section contains the steps to install Java on your system.

There are two ways to install Java on your system:.

1. Using downloaded JVM with PATH setup in the system environment variables
2. Using [SDKMAN](https://sdkman.io/)

## Downloading the JVM

### Pre-requisites

- Downloaded SAP JVM 8 archive: Ensure you've downloaded the correct SAP JVM 8 archive compatible with your operating
  system from the official SAP website (https://help.sap.com/docs/btp/sap-btp-neo-environment/optional-install-sap-jvm).

### Installation and Configuration

1. Extract the downloaded SAP JVM 8 archive to a directory of your choice.
2. Set the `JAVA_HOME` environment variable to the directory where the SAP JVM 8 is extracted.

#### Windows

- Right-click on `This PC` and select `Properties`.
- Click on `Advanced system settings`.
- Click on `Environment Variables`.
- Under `System variables`, click on `New`.
- Add `JAVA_HOME` as the variable name and the path to the extracted SAP JVM 8 directory as the variable value (
  e.g. `C:\SAPJVM8`).
- Click `OK` to save the changes.

#### Linux / macOS

- Open the terminal and run the following command to open the `.bashrc` or `.zshrc` file in the nano text editor:
    ```bash
    nano ~/.bashrc
    ```
  or
    ```bash
    nano ~/.zshrc
    ```

- Add the following line at the end of the file:
    ```bash
    export JAVA_HOME=/path/to/extracted/SAPJVM8
    export PATH=$JAVA_HOME/bin:$PATH
    ```

- Press `Ctrl + X` to exit the nano text editor and press `Y` to save the changes.
- Run the following command to apply the changes:
    ```bash
    source ~/.bashrc
    ```
  or
    ```bash
    source ~/.zshrc
    ```

3. Verify the installation by running the following command in the terminal:
    ```bash
    java -version
    ```

## Using SDKMAN (Java 11 and higher)

- SDKMAN! documentation is available at [https://sdkman.io/](https://sdkman.io/).
- Installation process is available at [https://sdkman.io/install](https://sdkman.io/install).

### Get list of available Java versions (optional)

- Run the following command in the terminal to get the list of available Java versions:
    ```bash
    sdk list java
    ```

### Listing versions

List of available Java versions can be found at [https://sdkman.io/jdks](https://sdkman.io/jdks).

Known versions at the time of writing this documentation are:

- 11.0.17-sapmchn
- 11.0.21-sapmchn
- 11.0.22-sapmchn
- 17.0.6-sapmchn
- 17.0.9-sapmchn
- 17.0.10-sapmchn
- 21.0.1-sapmchn
- 21.0.2-sapmchn

### Installation

- Run the following command in the terminal to install SAP JVM 8:
    ```bash
    sdk install java 11.0.17-sapmchn
    ```

# Maven Installation Guide

This documentation section contains the steps to install Maven on your system.

## Manual Installation

- [Documentation link](https://maven.apache.org/install.html)

## SDKMAN Installation

Actually, when using SDKMAN, Maven is installed automatically when installing Java. However, if you want to install
Maven
separately, you can do so by following the steps below.

- List available Maven versions:
    ```bash
    sdk list maven
    ```
- Run the following command in the terminal to install Maven:
    ```bash
    sdk install maven 3.8.6
    ```