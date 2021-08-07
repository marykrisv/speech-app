alter role postgres password 'password';
create database speech_dev with owner postgres;
\c speech_dev postgres;