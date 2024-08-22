Simple CLI Retrieval Augmented Generation Scanner
=================================================
Aim of the project: A showcase of a RAG scanner written in Java and using [Spring AI](https://docs.spring.io/spring-ai/reference/api/index.html), which scans the targeted documents and you can ask questions to the LLM regarding the given documents.

## Disclaimer
This tool is intended for educational and productivity purposes only. It is designed to assist users in managing and querying their own documents. Any illegal or unethical use of this software is strictly prohibited.

## Requirements
1. [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) installed on your device
2. [Docker](https://www.docker.com/products/docker-desktop/)
3. An environment variable named `GOOGLE_API_KEY` and add your [Google Gemini API key](https://ai.google.dev/gemini-api/docs/api-key)

## Installation
1. Navigate to the project directory
2. Open CMD/Powershell/Terminal
3. For Windows Run `./mvnw clean install`, for Linux/Mac run `./mvn clean install`

## How to use:
1. Run `docker-compose up` in your CMD/Powershell/Terminal
2. Run the project using maven, on Windows: `./mvnw spring-boot:run`, on Linux/Mac run `./mvn spring-boot:run`.
3. When the shell opens type `collection-size 768` (for Gemini `768` is compatible).
4. Place your files in a directory, copy the full path of the directory, and run something like this `load /your/path`, wait till the files are chunked and loaded to `Qdrant vector database`.
5. Finally in the shell write `ask "your question here"` and that's it.


### Notes
It's a simple project, needs a lot of improvements like: 
1. Improve chunking documents (Currently chunked by token size)
2. Support more file types (Currently supports txt, HTML, JSON, MD, docx, ppt, pdf, and a lot more)
3. Support other Chat models like GPT, Ollama, etc... (currently supports Gemini version `gemini-1.5-flash-latest`, the reason I decided to use Gemini is because it has a good free tier)
4. Support to make it a standalone executable and a jar file, (Currently you can build it yourself and run it, it has no problem, but I will simplify it)
5. Support other vector databases ( Currently supports Qdrant, to be honest, it's good enough)
6. Support custom System Context and custom similar returned documents in DB (Default, for now, is 5.)

#### Rabbit hole
Don't try to retrieve an API key from older `.git` versions, it's a rabbit hole :)

Please create an Issue, if something is wrong I will look into it, and feel free to contribute to the project.
==============


Shout-out to my friend Luxunator for the idea and everything.
