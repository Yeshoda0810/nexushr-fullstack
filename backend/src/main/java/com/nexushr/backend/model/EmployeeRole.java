package com.nexushr.backend.model;

/**
 * Roles drive what an employee can see/do.
 * ADMIN -> full access (HR head)
 * MANAGER -> can approve leave, view team performance
 * EMPLOYEE -> can view/apply for own data only
 */
public enum EmployeeRole {
    ADMIN,
    MANAGER,
    EMPLOYEE
}
