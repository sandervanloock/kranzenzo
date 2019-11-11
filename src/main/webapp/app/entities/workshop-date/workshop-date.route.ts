import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from './workshop-date.service';
import { WorkshopDateComponent } from './workshop-date.component';
import { WorkshopDateDetailComponent } from './workshop-date-detail.component';
import { WorkshopDateUpdateComponent } from './workshop-date-update.component';
import { WorkshopDateDeletePopupComponent } from './workshop-date-delete-dialog.component';
import { IWorkshopDate } from 'app/shared/model/workshop-date.model';

@Injectable({ providedIn: 'root' })
export class WorkshopDateResolve implements Resolve<IWorkshopDate> {
  constructor(private service: WorkshopDateService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkshopDate> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((workshopDate: HttpResponse<WorkshopDate>) => workshopDate.body));
    }
    return of(new WorkshopDate());
  }
}

export const workshopDateRoute: Routes = [
  {
    path: '',
    component: WorkshopDateComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopDate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WorkshopDateDetailComponent,
    resolve: {
      workshopDate: WorkshopDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopDate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WorkshopDateUpdateComponent,
    resolve: {
      workshopDate: WorkshopDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopDate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WorkshopDateUpdateComponent,
    resolve: {
      workshopDate: WorkshopDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopDate.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const workshopDatePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: WorkshopDateDeletePopupComponent,
    resolve: {
      workshopDate: WorkshopDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopDate.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
