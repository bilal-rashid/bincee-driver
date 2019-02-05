package com.bincee.driver.api.model.notification;

import com.bincee.driver.api.model.Student;

public class Notification {

    public static final int UPDATE_STATUS = 1;
    public static final int ATTANDACE = 2;
    public static final int RIDE = 3;


    public static class Notific {

        public String title;
        public String message;
        public String body;
        public Data data;
        public int type;


        public Notific(String title, String message, int type) {
            this.title = title;
            this.message = message;
            this.type = type;
            body = message;

        }

    }

    public static class Data {

        public int studentId;

        public Data(int studentId) {
            this.studentId = studentId;
        }
    }

    public static class OuterData {

        public int studentId;
        public int type;

        public OuterData(Notific notification) {
            studentId = notification.data.studentId;
            type = notification.type;
        }
    }

}
