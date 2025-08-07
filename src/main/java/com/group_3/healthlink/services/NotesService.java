package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.List;
import java.util.ArrayList;

public class NotesService {
  /**
   * Creates a new Note in the database.
   *
   * @param content the content of the note
   * @param userId  the ID of the user creating the note
   * @return true if the note was created successfully, false otherwise
   */
  public static boolean createNote(String content, int userId) {
    String query = "INSERT INTO note (content, user_id, timestamp) VALUES (?, ?, ?)";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, content);
      stmt.setInt(2, userId);
      stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error createNote: " + e.getMessage());
      return false;
    }
  }

  /**
   * Retrieves a list of Notes for a user.
   *
   * @param userId the ID of the user
   * @return an array of Notes for the user
   */
  public static Note[] getUserNotes(int userId) {
    String query = "SELECT * FROM note WHERE user_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();

      List<Note> notesList = new ArrayList<>();
      while (rs.next()) {
        Note note = new Note();
        note.setId(rs.getInt("note_id"));
        note.setUserId(rs.getInt("user_id"));
        note.setContent(rs.getString("content"));
        note.setTimestamp(rs.getTimestamp("timestamp"));
        notesList.add(note);
      }

      rs.close();
      stmt.close();

      return notesList.toArray(new Note[0]);
    } catch (Exception e) {
      System.err.println("Error getNotesByPatientId: " + e.getMessage());
      return new Note[0];
    }
  }

  /**
   * Deletes a Note by its ID.
   *
   * @param noteId the ID of the note to delete
   * @return true if the note was deleted successfully, false otherwise
   */
  public static boolean deleteNote(int noteId) {
    String query = "DELETE FROM note WHERE note_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, noteId);
      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error deleteNote: " + e.getMessage());
      return false;
    }
  }

  /**
   * Updates the content of a Note.
   *
   * @param noteId      the ID of the note to update
   * @param noteContent the new content for the note
   * @return true if the note was updated successfully, false otherwise
   */
  public static boolean updateNote(int noteId, String noteContent) {
    String query = "UPDATE note SET content = ? WHERE note_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, noteContent);
      stmt.setInt(2, noteId);

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error updateNote: " + e.getMessage());
      return false;
    }
  }
}
