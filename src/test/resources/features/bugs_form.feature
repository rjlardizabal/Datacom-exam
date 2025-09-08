@bugs-form @ui @regression
Feature: Bugs Form - Registration
  As a candidate for Datacom senior QA Engineer role
  I want to write an automated test script for the bugs-form page
  So that I can show my skills in writing automated test case

  Background:
    Given the Spot the Bugs Form is available to the user

    Scenario: Accepts a complete and valid submission
      When the user submits the form with:
        | firstName | Jane                 |
        | lastName  | Doe                  |
        | phone     | 6421234567           |
        | country   | New Zealand          |
        | email     | jane.doe@example.com |
        | password  | Secr3t!              |
      Then the submission is accepted
      And the following registration details are displayed:
        | First Name   | Jane                 |
        | Last Name    | Doe                  |
        | Phone Number | 6421234567           |
        | Country      | New Zealand                |
        | Email        | jane.doe@example.com |

    Scenario Outline: Missing required field is rejected
      When the user submits the form with <field> missing and all other fields valid
      Then a required-field message is shown for <field>

      Examples:
        | field         |
        | Last Name     |
        | Phone number  |
        | Email address |
        | Password      |


  Rule: Phone number must contain at least 10 digits
    Scenario Outline: Phone number length boundaries
      When the user submits the form with phone "<value>" and all other fields valid
      Then the form <result> a phone-length validation message

      Examples:
        | value       | result   |
        | 123456789   | shows    |
        | 1234567890  | does not show |
        | 12345678901 | does not show |

    Scenario: Non-digit characters are rejected for phone
      When the user submits the form with a phone containing non-digits (e.g., "+64-21-234-5678") and all other fields valid
      Then a phone-format or phone-length validation message is shown

    Scenario Outline: Password length boundaries
      When the user submits the form with password "<password>" and all other fields valid
      Then the form <result> a password-length validation message

      Examples:
        | password                 | result   |
        | short                    | shows    |
        | secret                   | does not show |
        | exactlyTwentyChars!!     | does not show |
        | moreThanTwentyCharacters | shows    |

    Scenario Outline: Email format validation
      When the user submits the form with email "<email>" and all other fields valid
      Then the form <result> an email-format validation message

      Examples:
        | email                 | result   |
        | plainaddress          | shows    |
        | @missing-local.com    | shows    |
        | name@domain           | shows    |
        | name@domain.c         | shows    |
        | name@domain.com       | does not |
        | first.last@sub.dom.nz | does not |

