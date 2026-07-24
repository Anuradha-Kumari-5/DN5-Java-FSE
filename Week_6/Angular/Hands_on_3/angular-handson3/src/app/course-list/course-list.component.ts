import { Component, OnInit } from '@angular/core';

@Component({
  selector:'app-course-list',
  templateUrl:'./course-list.component.html'
})
export class CourseListComponent implements OnInit{
  isLoading=true;
  courses=[
    {id:1,title:'Angular',credits:3,enrolled:true,gradeStatus:'passed'},
    {id:2,title:'Java',credits:4,enrolled:false,gradeStatus:'pending'},
    {id:3,title:'Python',credits:1,enrolled:true,gradeStatus:'failed'}
  ];

  ngOnInit(): void{
    setTimeout(()=>this.isLoading=false,1500);
  }

  // trackBy improves performance by reusing DOM nodes instead of recreating them.
  trackByCourseId(index:number,course:any){
    return course.id;
  }
}
