package smartcitas.medical_records.repository.model;

public class MedicalRecord {
    private int idRecord;
    private int idPatient;
    private int idDoctor;
    private String diagnosis;
    private String treatment;
    private String notes;

    public MedicalRecord() {
    }

    public MedicalRecord(int idRecord, int idPatient, int idDoctor, String diagnosis, String treatment, String notes) {
        this.idRecord = idRecord;
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
