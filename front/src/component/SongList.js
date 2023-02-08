import React from 'react';

const SongList = ({ songs }) => (
  <ul>
    {songs.map((song) => (
      <li key={song.id}>
        <h3>{song.title}</h3>
        <p>by {song.author}</p>
      </li>
    ))}
  </ul>
);

export default SongList;
