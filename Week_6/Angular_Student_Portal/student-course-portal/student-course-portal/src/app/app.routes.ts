import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CourseListComponent } from './pages/course-list/course-list.component';
import { StudentProfileComponent } from './pages/student-profile/student-profile.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'courses', component: CourseListComponent },
  { path: 'profile', component: StudentProfileComponent }
];
