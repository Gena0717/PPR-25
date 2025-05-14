# Multimodal_Parliament_Explorer_11_3

## Authors
Ishak Bouaziz 6774123\
Genadij Vorontsov 7697757\
Sophie Renate Hildegard Kaiser 6949963\
Oliwia Daszczynska 6950239

## Description
The Multimodal Parliament Explorer is a full-stack application developed as the final group project for the Programming Practicum module in the Bachelor’s program in Computer Science at Goethe University. The goal of the project is to design and implement a comprehensive system for the acquisition, processing, analysis, and visualization of German Bundestag parliamentary debates from the 20th legislative period.

## Project Overview
The application consists of a Java-based backend and a web-based frontend, incorporating modern web technologies and natural language processing tools to enable multimodal exploration of parliamentary data, including text, metadata, and video content.

## Features
Backend (Java 17, Javalin, MongoDB)

- Automatic download and import of XML Bundestag protocols and associated video material.
- Structured storage of speeches, speaker metadata, and session information.
- NLP preprocessing pipeline using Docker Unified UIMA Interface (DUUI), supporting:
  - Tokenization, sentence segmentation, POS tagging, dependency parsing, lemmatization
  - Named entity recognition (NER)
  - Topic modeling (ParlBERT-v2)
  - Sentiment analysis (GerVader)
  - Video transcription (WhisperX)
- RESTful API built with Javalin, documented using Swagger.
- Speaker photo integration via Bundestag image database.

Frontend (FreeMarker, d3.js, jQuery)

- Interactive dashboards and search interface.
- Visualizations:
  -  Bubble Chart: Topic distribution
  -  Bar Chart: POS tagging
  -  Radar Chart: Sentiment analysis
  -  Sunburst Chart: Named entity categories
- Detailed speech view:
  - NLP annotations highlighted in text
  - Sentiment icons per sentence
  - Speaker profile with photo and party affiliation
  - Synchronized video with sentence-level highlighting

## How to use
In order to run the application, you need to ensure that the source folder is marked as a root-source folder in your IDE.
Source-Folder: `src/main/java`
Resources-Folder: `src/main/resources`
Diagrams and Documentation: `/diagrams_and_documentation` 

## Sources
- https://www.btg-bestellservice.de/informationsmaterial/anr80140010
  (last access: 2024-11-09,14:50) for translations of the German Parliamentary Vocabulary\
...

## Translations
| English translation       | German original    |
|---------------------------|--------------------|
| academic title            | Akad Titel         |
| address title             | Anrede Titel       |
| agenda item               | Tagesordnungspunkt |
| country of birth          | Geburtsland        |
| curriculum vitae          | Vita               |
| date of birth             | Geburtsdatum       |
| date of death             | Sterbedatum        |
| first name                | Vorname            |
| gender                    | Geschlecht         |
| (parliamentary) group     | Fraktion           |
| grouping                  | Gruppe             |
| guest                     | Gast               |
| last name                 | Nachname           |
| legislative term          | Legislaturperiode  |
| location                  | Ortszusatz         |
| marital status            | Familienstand      |
| member (of the Bundestag) | Abgeordnete\*r     |
| nobility                  | Adel               |
| participant               | Teilnehmer         |
| party                     | Partei             |
| place of birth            | Geburtsort         |
| prefix                    | Praefix            |
| profession                | Beruf              |
| religion                  | Religion           |
| session                   | Sitzung            |
| speaker                   | Redner\*in         |
| speech                    | Rede               |
