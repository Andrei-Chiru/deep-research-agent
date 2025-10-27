# Brutus 
- deep research agent that analyses choices in brief Request for Comments style 
## Table of contents
## Overview
The deep-research-agent is a Java-based framework designed to automate and structure research workflows in RFC-style.
It is intended to break down complex questions or problems into sub-tasks, apply agentic reasoning (e.g., planning, iteration, tool invocation) and then synthesise findings into a coherent output.
It suits environments where you want more control and observability over the research process rather than a black-box LLM call.
## Features
* Task decomposition: the agent analyses a higher-level request and breaks it into logical subtasks. 
* Structured reports: output follows a semi-formal format (RFC-style) allowing for decisions, reasoning, trade-offs and summary. 
* Java implementation: leveraging the Java ecosystem for reliability, tooling, integration (Gradle).
## Prerequisites
* Java 21
* Gradle
* OpenAI API key
## Installation
1) Clone the repository
```bash
git clone https://github.com/Andrei-Chiru/deep-research-agent.git  
cd deep-research-agent
```
2) Create a .env file in the depp-research-agent folder and put the key
```text
OPENAI_API_KEY=...
```
3) Run the agent
```bash
./gradlew run
```
## Usage
1) The user supplies a query comparing two terms.
2) The planner enforces a task breakdown in 6 stages: plan how to answer, gather information, draft the answer, verify information, repair the knowledge base, finalize the report.
3) The user can either compare other two terms or stop the application.
4) The reports will include:
* TL;DR
* Background
* Analysis by criteria
* Trade-offs
* Risks
* Cost/Operational considerations
* Recommendation by use case
* References section
## Example queries
```text
What programming language should I use - python or java?
```
```text
Should I eat pizza or pasta tonight?
```
