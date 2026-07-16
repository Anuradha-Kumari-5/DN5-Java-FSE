# Setup Notes — ReactiveEnrollmentFormComponent

## 1. Generate the component (if not already done)
```
ng generate component pages/reactive-enrollmentform
```
Then replace the generated `.ts`, `.html`, `.css` files with the ones in this folder.

## 2. Import ReactiveFormsModule
In `app.module.ts` (or the module that declares this component):
```ts
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    // ...other imports
    ReactiveFormsModule
  ]
})
export class AppModule {}
```

## 3. Add the route
In `app-routing.module.ts`:
```ts
import { ReactiveEnrollmentformComponent } from './pages/reactive-enrollmentform/reactive-enrollmentform.component';

const routes: Routes = [
  // ...existing routes
  { path: 'enroll-reactive', component: ReactiveEnrollmentformComponent }
];
```

## 4. Test
Navigate to `/enroll-reactive`. Fill the form — Submit stays disabled until:
- studentName ≥ 3 characters
- studentEmail is a valid email
- courseId is selected
- agreeToTerms is checked

Check the browser console on submit to see `enrollForm.value` vs `enrollForm.getRawValue()`.
