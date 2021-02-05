package com.example.user.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueModel
{

    private String id;

    private String desription;

    private String detail;

    private String date;

    private String assignee_name;

    private String project_name;

}
