# Java Big Data Labs

Java learning labs for big data and machine learning experiments.

## Purpose

This repository keeps hands-on Java exercises around Hadoop-style data processing and related machine learning practice. It is a lab/reference repo, not a production data platform.

## Contents

- Java source code for data-processing exercises.
- Input resources used by local experiments.
- Supporting project configuration for IDE-based exploration.

## How To Use

Open the project in IntelliJ IDEA and run specific examples from the IDE. Some examples may require local datasets under `src/main/resources/input`.

## Status

Learning lab. The main risk is mixed local data/artifacts; the next upgrade is to document dataset provenance and separate sample data from generated outputs.

## Validation

For each lab, record the input dataset, expected output shape, and whether generated artifacts should be committed or ignored.

## Engineering Notes

Separate source, sample data, and generated output. Big-data labs become hard to trust when local artifacts and source files are mixed.
