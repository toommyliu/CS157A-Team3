package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Note;

import java.sql.*;

public class NotesService {
  public static boolean createNote(String content, int patientId, int doctorId) {
    String query = "INSERT INTO note (content, patient_id, doctor_id, created_at) VALUES (?, ?, ?, ?)";
    java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, content);
      stmt.setInt(2, patientId);

      if (doctorId != -1) {
        stmt.setInt(3, doctorId);
      } else {
        stmt.setNull(3, Types.INTEGER);
      }

      stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      con.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error createNote: " + e.getMessage());
      return false;
    }
  }

  public static Note[] getNotesByPatientId(int patientId) {
    String query = "SELECT * FROM note WHERE patient_id = ?";
    java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, patientId);
      ResultSet rs = stmt.executeQuery();

      java.util.List<Note> notesList = new java.util.ArrayList<>();
      while (rs.next()) {
        Note note = new Note();
        note.setId(rs.getInt("note_id"));
        note.setPatientId(rs.getInt("patient_id"));
        note.setDoctorId(rs.getInt("doctor_id"));
        note.setContent(rs.getString("content"));
        note.setCreatedAt(rs.getTimestamp("created_at"));
        notesList.add(note);
      }

      rs.close();
      stmt.close();
      con.close();

      return notesList.toArray(new Note[0]);
    } catch (Exception e) {
      System.err.println("Error getNotesByPatientId: " + e.getMessage());
      return new Note[0];
    }
  }
}

