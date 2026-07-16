import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-reactive-enrollmentform',
  templateUrl: './reactive-enrollmentform.component.html',
  styleUrls: ['./reactive-enrollmentform.component.css']
})
export class ReactiveEnrollmentformComponent implements OnInit {

  enrollForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.enrollForm = this.fb.group({
      studentName: ['', [Validators.required, Validators.minLength(3)]],
      studentEmail: ['', [Validators.required, Validators.email]],
      courseId: [null, Validators.required],
      preferredSemester: ['Odd', Validators.required],
      agreeToTerms: [false, Validators.requiredTrue]
    });
  }

  onSubmit(): void {
    // enrollForm.value  -> returns ONLY the values of ENABLED controls.
    //                      Any control that has been disabled (e.g. via
    //                      control.disable()) is silently excluded from
    //                      this object. This is what you normally send
    //                      to the backend, since disabled fields usually
    //                      represent data the user isn't allowed to edit.
    //
    // enrollForm.getRawValue() -> returns the values of ALL controls,
    //                      including disabled ones. Useful when you need
    //                      the complete form model (e.g. for local state,
    //                      debugging, or pre-filling a summary view) even
    //                      if some fields are currently disabled in the UI.
    console.log('enrollForm.value:', this.enrollForm.value);
    console.log('enrollForm.getRawValue():', this.enrollForm.getRawValue());
  }
}
