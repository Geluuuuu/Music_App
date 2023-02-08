#!/bin/bash
psql -v ON_ERROR_STOP=1 -U postgres <<-EOSQL
  \connect music_db
  BEGIN;
    CREATE TYPE Genre as enum('Rock', 'Pop');
    CREATE TYPE TypeMusic as enum('album', 'song', 'single');
    CREATE TABLE IF NOT EXISTS content (
      id serial NOT NULL,
      name VARCHAR(100),
      gen VARCHAR(100),
      year Int,
      type VARCHAR(100)
	);
	CREATE TABLE IF NOT EXISTS artists (
	   id uuid NOT NULL,
	   name VARCHAR(100),
	   active BOOLEAN
	);
	CREATE TABLE IF NOT EXISTS content_artist (
	  content_id INT NOT NULL,
	  artist_id uuid NOT NULL
	);
  COMMIT;
EOSQL