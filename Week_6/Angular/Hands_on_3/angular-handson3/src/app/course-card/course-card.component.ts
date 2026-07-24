import { Component, Input } from '@angular/core';

@Component({
 selector:'app-course-card',
 templateUrl:'./course-card.component.html',
 styleUrls:['./course-card.component.css']
})
export class CourseCardComponent{
 @Input() course:any;
 isExpanded=false;

 // Getter keeps template clean and avoids repeating object literals.
 get cardClasses(){
   return {
     'card--enrolled':this.course?.enrolled,
     'card--full':this.course?.credits>=4,
     'expanded':this.isExpanded
   };
 }

 get borderColor(){
   switch(this.course?.gradeStatus){
     case 'passed': return 'green';
     case 'failed': return 'red';
     default: return 'grey';
   }
 }

 toggleDetails(){
   this.isExpanded=!this.isExpanded;
 }
}
