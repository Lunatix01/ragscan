# ragscan initial design doc

## Objective

The overall goal of this project is to test the application of Retreival Augmented Generation (RAG) to the reconnaissance phase of pentesting. RAG has shown
to be effective in giving Large Language Models (LLM's) dynamic information in which it is not trained on for use to answer the users prompts and instructions.

## Overview
This document will show the high level design of the initial approach taken for this tool.

## Processing of File and Document Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/f56a6f25-44bc-4d77-88f5-318fdfd0865c)

### Scrape Web Content
As of now, there is not any plans to add any tooling that will do the actual scraping of web content as it would put less focus on the core functionality
of this project and there are other tools that can do this much better and have had more project maturity. Many of these tools can be found in https://github.com/BruceDone/awesome-crawler.

### Input/Scraped Web Content 
The expected input to the tool are any pages, code, or information that was gathered through other tools and are stored in a directory in their original files and file 
structure. This ensures that there is tracking as to where information that the LLM processes is from and is easily identifiable for the user if they wish to manually inspect
the identified file themselves after tool usage.

### Initial Loading of Content
On execution of the tool, it will load all of the files in the specified directory, open them, convert them into standardized document objects that a vector
database will be able to identify and use.

### Document to Embeddings Conversion
After each of the files are loaded as documents, they can be converted into [embeddings](https://www.cloudflare.com/learning/ai/what-are-embeddings/) which is a unique
representation of that specific object that can be used for more complex processing. This conversion is done through the use of [text embedding models](https://medium.com/@minh.hoque/text-embedding-models-an-insightful-dive-759ea53576f5) thats sole purpose is to create the vectorized representation of chunks of text.

### Storing the Embeddings
After all of the previous processing is done, the generated embeddings can be stored within a [vector database](https://aws.amazon.com/what-is/vector-databases/) that's
specialized in the ability to detect the similarity of object and content based on the space between their vector point location. This is an important prerequisite to the
success of this RAG approach where there can large amounts of documents that together would be larger than the token limit of current LLM's. 

## Standard Flow on Tool Usage on User Prompt Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/472ee53d-7ccb-4f96-bd56-d1e81bd58c05)

### User Prompt
The user prompt is the other input to this tool that is highly responsible for the output in which the vector database returns and for the LLM to process. An example prompt that
a pentester may have when having a large set of documents are are things like "is there an admin login page." or "what is the version of wordpress that this website is running on?".
After this user prompt is gathered, the tool can start it execution

### User Prompt Input Conversion to Embedding
Because the documents gathered are now stored in the vector database alongside their embeddings, we need to convert the user input prompt into an embedding as well to calculate
similarity. This is done through the use of an embedding model just as explained above with the conversion of the documents into embeddings

### Semantic Search Using Embeddings
Once the user prompt input is converted into embeddings, it is now possible to use [semantic search](https://www.elastic.co/what-is/semantic-search) to find the relevant documents that are most similar to the user input prompt.

### Semantic Search Results
Once the semantic search has been completed using the embeddings, the output should be the most relevant documents in relation to the users input prompt that are ranked on the
calculated relevancy to it. For example, if a user is asking for if there is an admin login page, the top result would most likely be the admin login page document, with lower
ranked results possibly being other login pages or mentions of admins.

### Construct the Prompt to Feed to the LLM
After gathering the most probable content that matches a users input, we can not just feed that directly to the LLM without any other context as it would not know exactly what the
user wants. There are two other parts that will need to be added to the prompt to the LLM, one being the user input prompt, and another being a system prompt. The user input prompt
is to guide the LLM to do the exact task that the user would like, such as identifying if there is an admin page. The system prompt is used to guide the LLM to answer in a specific
manner and to understand the context that the user is conducting a pentest and may want a more concise response. Putting these three things together creates the full prompt that will
be fed to the LLM.

### Feeding Prompt to LLM
The constructed prompt is then fed to the LLM for it to summarize and provide specific context to the user. Depending on the system prompt given to the LLM, its response could be just the fact that it identified a possible result, or it can go further and explain why it things that it is or is not what the user is looking for. This is the most unpredictable step but
highly valuable given that the prompt is correct and no alignment in the LLM renders the results unusable.

### Return the Output to the User
After the LLM has done what it was tasked with doing, the generated response should return to the user alongside the location or identifier of the files used as input as part of the prompt to the LLM. This could be the location of the admin document in the directory that was used to feed documents into the vector database, or even more verbose identifiers such as line numbers or file hashes. This output should give the output that the user was looking for and making the recon process much easier.


# ragscan initial design doc

## Objective

The overall goal of this project is to test the application of Retreival Augmented Generation (RAG) to the reconnaissance phase of pentesting. RAG has shown
to be effective in giving Large Language Models (LLM's) dynamic information in which it is not trained on for use to answer the users prompts and instructions.

## Overview
This document will show the high level design of the initial approach taken for this tool.

## Processing of File and Document Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/f56a6f25-44bc-4d77-88f5-318fdfd0865c)

### Scrape Web Content
As of now, there is not any plans to add any tooling that will do the actual scraping of web content as it would put less focus on the core functionality
of this project and there are other tools that can do this much better and have had more project maturity. Many of these tools can be found in https://github.com/BruceDone/awesome-crawler.

### Input/Scraped Web Content
The expected input to the tool are any pages, code, or information that was gathered through other tools and are stored in a directory in their original files and file
structure. This ensures that there is tracking as to where information that the LLM processes is from and is easily identifiable for the user if they wish to manually inspect
the identified file themselves after tool usage.

### Initial Loading of Content
On execution of the tool, it will load all of the files in the specified directory, open them, convert them into standardized document objects that a vector
database will be able to identify and use.

### Document to Embeddings Conversion
After each of the files are loaded as documents, they can be converted into [embeddings](https://www.cloudflare.com/learning/ai/what-are-embeddings/) which is a unique
representation of that specific object that can be used for more complex processing. This conversion is done through the use of [text embedding models](https://medium.com/@minh.hoque/text-embedding-models-an-insightful-dive-759ea53576f5) thats sole purpose is to create the vectorized representation of chunks of text.

### Storing the Embeddings
After all of the previous processing is done, the generated embeddings can be stored within a [vector database](https://aws.amazon.com/what-is/vector-databases/) that's
specialized in the ability to detect the similarity of object and content based on the space between their vector point location. This is an important prerequisite to the
success of this RAG approach where there can large amounts of documents that together would be larger than the token limit of current LLM's.

## Standard Flow on Tool Usage on User Prompt Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/472ee53d-7ccb-4f96-bd56-d1e81bd58c05)

### User Prompt
The user prompt is the other input to this tool that is highly responsible for the output in which the vector database returns and for the LLM to process. An example prompt that
a pentester may have when having a large set of documents are are things like "is there an admin login page." or "what is the version of wordpress that this website is running on?".
After this user prompt is gathered, the tool can start it execution

### User Prompt Input Conversion to Embedding
Because the documents gathered are now stored in the vector database alongside their embeddings, we need to convert the user input prompt into an embedding as well to calculate
similarity. This is done through the use of an embedding model just as explained above with the conversion of the documents into embeddings

### Semantic Search Using Embeddings
Once the user prompt input is converted into embeddings, it is now possible to use [semantic search](https://www.elastic.co/what-is/semantic-search) to find the relevant documents that are most similar to the user input prompt.

### Semantic Search Results
Once the semantic search has been completed using the embeddings, the output should be the most relevant documents in relation to the users input prompt that are ranked on the
calculated relevancy to it. For example, if a user is asking for if there is an admin login page, the top result would most likely be the admin login page document, with lower
ranked results possibly being other login pages or mentions of admins.

### Construct the Prompt to Feed to the LLM
After gathering the most probable content that matches a users input, we can not just feed that directly to the LLM without any other context as it would not know exactly what the
user wants. There are two other parts that will need to be added to the prompt to the LLM, one being the user input prompt, and another being a system prompt. The user input prompt
is to guide the LLM to do the exact task that the user would like, such as identifying if there is an admin page. The system prompt is used to guide the LLM to answer in a specific
manner and to understand the context that the user is conducting a pentest and may want a more concise response. Putting these three things together creates the full prompt that will
be fed to the LLM.

### Feeding Prompt to LLM
The constructed prompt is then fed to the LLM for it to summarize and provide specific context to the user. Depending on the system prompt given to the LLM, its response could be just the fact that it identified a possible result, or it can go further and explain why it things that it is or is not what the user is looking for. This is the most unpredictable step but
highly valuable given that the prompt is correct and no alignment in the LLM renders the results unusable.

### Return the Output to the User
After the LLM has done what it was tasked with doing, the generated response should return to the user alongside the location or identifier of the files used as input as part of the prompt to the LLM. This could be the location of the admin document in the directory that was used to feed documents into the vector database, or even more verbose identifiers such as line numbers or file hashes. This output should give the output that the user was looking for and making the recon process much easier.

# ragscan initial design doc

## Objective

The overall goal of this project is to test the application of Retreival Augmented Generation (RAG) to the reconnaissance phase of pentesting. RAG has shown
to be effective in giving Large Language Models (LLM's) dynamic information in which it is not trained on for use to answer the users prompts and instructions.

## Overview
This document will show the high level design of the initial approach taken for this tool.

## Processing of File and Document Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/f56a6f25-44bc-4d77-88f5-318fdfd0865c)

### Scrape Web Content
As of now, there is not any plans to add any tooling that will do the actual scraping of web content as it would put less focus on the core functionality
of this project and there are other tools that can do this much better and have had more project maturity. Many of these tools can be found in https://github.com/BruceDone/awesome-crawler.

### Input/Scraped Web Content
The expected input to the tool are any pages, code, or information that was gathered through other tools and are stored in a directory in their original files and file
structure. This ensures that there is tracking as to where information that the LLM processes is from and is easily identifiable for the user if they wish to manually inspect
the identified file themselves after tool usage.

### Initial Loading of Content
On execution of the tool, it will load all of the files in the specified directory, open them, convert them into standardized document objects that a vector
database will be able to identify and use.

### Document to Embeddings Conversion
After each of the files are loaded as documents, they can be converted into [embeddings](https://www.cloudflare.com/learning/ai/what-are-embeddings/) which is a unique
representation of that specific object that can be used for more complex processing. This conversion is done through the use of [text embedding models](https://medium.com/@minh.hoque/text-embedding-models-an-insightful-dive-759ea53576f5) thats sole purpose is to create the vectorized representation of chunks of text.

### Storing the Embeddings
After all of the previous processing is done, the generated embeddings can be stored within a [vector database](https://aws.amazon.com/what-is/vector-databases/) that's
specialized in the ability to detect the similarity of object and content based on the space between their vector point location. This is an important prerequisite to the
success of this RAG approach where there can large amounts of documents that together would be larger than the token limit of current LLM's.

## Standard Flow on Tool Usage on User Prompt Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/472ee53d-7ccb-4f96-bd56-d1e81bd58c05)

### User Prompt
The user prompt is the other input to this tool that is highly responsible for the output in which the vector database returns and for the LLM to process. An example prompt that
a pentester may have when having a large set of documents are are things like "is there an admin login page." or "what is the version of wordpress that this website is running on?".
After this user prompt is gathered, the tool can start it execution

### User Prompt Input Conversion to Embedding
Because the documents gathered are now stored in the vector database alongside their embeddings, we need to convert the user input prompt into an embedding as well to calculate
similarity. This is done through the use of an embedding model just as explained above with the conversion of the documents into embeddings

### Semantic Search Using Embeddings
Once the user prompt input is converted into embeddings, it is now possible to use [semantic search](https://www.elastic.co/what-is/semantic-search) to find the relevant documents that are most similar to the user input prompt.

### Semantic Search Results
Once the semantic search has been completed using the embeddings, the output should be the most relevant documents in relation to the users input prompt that are ranked on the
calculated relevancy to it. For example, if a user is asking for if there is an admin login page, the top result would most likely be the admin login page document, with lower
ranked results possibly being other login pages or mentions of admins.

### Construct the Prompt to Feed to the LLM
After gathering the most probable content that matches a users input, we can not just feed that directly to the LLM without any other context as it would not know exactly what the
user wants. There are two other parts that will need to be added to the prompt to the LLM, one being the user input prompt, and another being a system prompt. The user input prompt
is to guide the LLM to do the exact task that the user would like, such as identifying if there is an admin page. The system prompt is used to guide the LLM to answer in a specific
manner and to understand the context that the user is conducting a pentest and may want a more concise response. Putting these three things together creates the full prompt that will
be fed to the LLM.

### Feeding Prompt to LLM
The constructed prompt is then fed to the LLM for it to summarize and provide specific context to the user. Depending on the system prompt given to the LLM, its response could be just the fact that it identified a possible result, or it can go further and explain why it things that it is or is not what the user is looking for. This is the most unpredictable step but
highly valuable given that the prompt is correct and no alignment in the LLM renders the results unusable.

### Return the Output to the User
After the LLM has done what it was tasked with doing, the generated response should return to the user alongside the location or identifier of the files used as input as part of the prompt to the LLM. This could be the location of the admin document in the directory that was used to feed documents into the vector database, or even more verbose identifiers such as line numbers or file hashes. This output should give the output that the user was looking for and making the recon process much easier.

# ragscan initial design doc

## Objective

The overall goal of this project is to test the application of Retreival Augmented Generation (RAG) to the reconnaissance phase of pentesting. RAG has shown
to be effective in giving Large Language Models (LLM's) dynamic information in which it is not trained on for use to answer the users prompts and instructions.

## Overview
This document will show the high level design of the initial approach taken for this tool.

## Processing of File and Document Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/f56a6f25-44bc-4d77-88f5-318fdfd0865c)

### Scrape Web Content
As of now, there is not any plans to add any tooling that will do the actual scraping of web content as it would put less focus on the core functionality
of this project and there are other tools that can do this much better and have had more project maturity. Many of these tools can be found in https://github.com/BruceDone/awesome-crawler.

### Input/Scraped Web Content
The expected input to the tool are any pages, code, or information that was gathered through other tools and are stored in a directory in their original files and file
structure. This ensures that there is tracking as to where information that the LLM processes is from and is easily identifiable for the user if they wish to manually inspect
the identified file themselves after tool usage.

### Initial Loading of Content
On execution of the tool, it will load all of the files in the specified directory, open them, convert them into standardized document objects that a vector
database will be able to identify and use.

### Document to Embeddings Conversion
After each of the files are loaded as documents, they can be converted into [embeddings](https://www.cloudflare.com/learning/ai/what-are-embeddings/) which is a unique
representation of that specific object that can be used for more complex processing. This conversion is done through the use of [text embedding models](https://medium.com/@minh.hoque/text-embedding-models-an-insightful-dive-759ea53576f5) thats sole purpose is to create the vectorized representation of chunks of text.

### Storing the Embeddings
After all of the previous processing is done, the generated embeddings can be stored within a [vector database](https://aws.amazon.com/what-is/vector-databases/) that's
specialized in the ability to detect the similarity of object and content based on the space between their vector point location. This is an important prerequisite to the
success of this RAG approach where there can large amounts of documents that together would be larger than the token limit of current LLM's.

## Standard Flow on Tool Usage on User Prompt Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/472ee53d-7ccb-4f96-bd56-d1e81bd58c05)

### User Prompt
The user prompt is the other input to this tool that is highly responsible for the output in which the vector database returns and for the LLM to process. An example prompt that
a pentester may have when having a large set of documents are are things like "is there an admin login page." or "what is the version of wordpress that this website is running on?".
After this user prompt is gathered, the tool can start it execution

### User Prompt Input Conversion to Embedding
Because the documents gathered are now stored in the vector database alongside their embeddings, we need to convert the user input prompt into an embedding as well to calculate
similarity. This is done through the use of an embedding model just as explained above with the conversion of the documents into embeddings

### Semantic Search Using Embeddings
Once the user prompt input is converted into embeddings, it is now possible to use [semantic search](https://www.elastic.co/what-is/semantic-search) to find the relevant documents that are most similar to the user input prompt.

### Semantic Search Results
Once the semantic search has been completed using the embeddings, the output should be the most relevant documents in relation to the users input prompt that are ranked on the
calculated relevancy to it. For example, if a user is asking for if there is an admin login page, the top result would most likely be the admin login page document, with lower
ranked results possibly being other login pages or mentions of admins.

### Construct the Prompt to Feed to the LLM
After gathering the most probable content that matches a users input, we can not just feed that directly to the LLM without any other context as it would not know exactly what the
user wants. There are two other parts that will need to be added to the prompt to the LLM, one being the user input prompt, and another being a system prompt. The user input prompt
is to guide the LLM to do the exact task that the user would like, such as identifying if there is an admin page. The system prompt is used to guide the LLM to answer in a specific
manner and to understand the context that the user is conducting a pentest and may want a more concise response. Putting these three things together creates the full prompt that will
be fed to the LLM.

### Feeding Prompt to LLM
The constructed prompt is then fed to the LLM for it to summarize and provide specific context to the user. Depending on the system prompt given to the LLM, its response could be just the fact that it identified a possible result, or it can go further and explain why it things that it is or is not what the user is looking for. This is the most unpredictable step but
highly valuable given that the prompt is correct and no alignment in the LLM renders the results unusable.

### Return the Output to the User
After the LLM has done what it was tasked with doing, the generated response should return to the user alongside the location or identifier of the files used as input as part of the prompt to the LLM. This could be the location of the admin document in the directory that was used to feed documents into the vector database, or even more verbose identifiers such as line numbers or file hashes. This output should give the output that the user was looking for and making the recon process much easier.

# ragscan initial design doc

## Objective

The overall goal of this project is to test the application of Retreival Augmented Generation (RAG) to the reconnaissance phase of pentesting. RAG has shown
to be effective in giving Large Language Models (LLM's) dynamic information in which it is not trained on for use to answer the users prompts and instructions.

## Overview
This document will show the high level design of the initial approach taken for this tool.

## Processing of File and Document Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/f56a6f25-44bc-4d77-88f5-318fdfd0865c)

### Scrape Web Content
As of now, there is not any plans to add any tooling that will do the actual scraping of web content as it would put less focus on the core functionality
of this project and there are other tools that can do this much better and have had more project maturity. Many of these tools can be found in https://github.com/BruceDone/awesome-crawler.

### Input/Scraped Web Content
The expected input to the tool are any pages, code, or information that was gathered through other tools and are stored in a directory in their original files and file
structure. This ensures that there is tracking as to where information that the LLM processes is from and is easily identifiable for the user if they wish to manually inspect
the identified file themselves after tool usage.

### Initial Loading of Content
On execution of the tool, it will load all of the files in the specified directory, open them, convert them into standardized document objects that a vector
database will be able to identify and use.

### Document to Embeddings Conversion
After each of the files are loaded as documents, they can be converted into [embeddings](https://www.cloudflare.com/learning/ai/what-are-embeddings/) which is a unique
representation of that specific object that can be used for more complex processing. This conversion is done through the use of [text embedding models](https://medium.com/@minh.hoque/text-embedding-models-an-insightful-dive-759ea53576f5) thats sole purpose is to create the vectorized representation of chunks of text.

### Storing the Embeddings
After all of the previous processing is done, the generated embeddings can be stored within a [vector database](https://aws.amazon.com/what-is/vector-databases/) that's
specialized in the ability to detect the similarity of object and content based on the space between their vector point location. This is an important prerequisite to the
success of this RAG approach where there can large amounts of documents that together would be larger than the token limit of current LLM's.

## Standard Flow on Tool Usage on User Prompt Input
![image](https://github.com/WeebSoftware/ragscan/assets/50147562/472ee53d-7ccb-4f96-bd56-d1e81bd58c05)

### User Prompt
The user prompt is the other input to this tool that is highly responsible for the output in which the vector database returns and for the LLM to process. An example prompt that
a pentester may have when having a large set of documents are are things like "is there an admin login page." or "what is the version of wordpress that this website is running on?".
After this user prompt is gathered, the tool can start it execution

### User Prompt Input Conversion to Embedding
Because the documents gathered are now stored in the vector database alongside their embeddings, we need to convert the user input prompt into an embedding as well to calculate
similarity. This is done through the use of an embedding model just as explained above with the conversion of the documents into embeddings

### Semantic Search Using Embeddings
Once the user prompt input is converted into embeddings, it is now possible to use [semantic search](https://www.elastic.co/what-is/semantic-search) to find the relevant documents that are most similar to the user input prompt.

### Semantic Search Results
Once the semantic search has been completed using the embeddings, the output should be the most relevant documents in relation to the users input prompt that are ranked on the
calculated relevancy to it. For example, if a user is asking for if there is an admin login page, the top result would most likely be the admin login page document, with lower
ranked results possibly being other login pages or mentions of admins.

### Construct the Prompt to Feed to the LLM
After gathering the most probable content that matches a users input, we can not just feed that directly to the LLM without any other context as it would not know exactly what the
user wants. There are two other parts that will need to be added to the prompt to the LLM, one being the user input prompt, and another being a system prompt. The user input prompt
is to guide the LLM to do the exact task that the user would like, such as identifying if there is an admin page. The system prompt is used to guide the LLM to answer in a specific
manner and to understand the context that the user is conducting a pentest and may want a more concise response. Putting these three things together creates the full prompt that will
be fed to the LLM.

### Feeding Prompt to LLM
The constructed prompt is then fed to the LLM for it to summarize and provide specific context to the user. Depending on the system prompt given to the LLM, its response could be just the fact that it identified a possible result, or it can go further and explain why it things that it is or is not what the user is looking for. This is the most unpredictable step but
highly valuable given that the prompt is correct and no alignment in the LLM renders the results unusable.

### Return the Output to the User
After the LLM has done what it was tasked with doing, the generated response should return to the user alongside the location or identifier of the files used as input as part of the prompt to the LLM. This could be the location of the admin document in the directory that was used to feed documents into the vector database, or even more verbose identifiers such as line numbers or file hashes. This output should give the output that the user was looking for and making the recon process much easier.
