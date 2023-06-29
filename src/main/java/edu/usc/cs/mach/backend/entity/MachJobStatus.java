package edu.usc.cs.mach.backend.entity;

public enum MachJobStatus {
     PENDING,
    SUCCESS,
    FAIL,
    FETCHED,

    //cancel a job when there is a newer version of the same job
    CANCELLED
}
