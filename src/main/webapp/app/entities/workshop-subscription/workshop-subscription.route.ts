import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { WorkshopSubscriptionService } from './workshop-subscription.service';
import { WorkshopSubscriptionComponent } from './workshop-subscription.component';
import { WorkshopSubscriptionDetailComponent } from './workshop-subscription-detail.component';
import { WorkshopSubscriptionUpdateComponent } from './workshop-subscription-update.component';
import { WorkshopSubscriptionDeletePopupComponent } from './workshop-subscription-delete-dialog.component';
import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

@Injectable({ providedIn: 'root' })
export class WorkshopSubscriptionResolve implements Resolve<IWorkshopSubscription> {
  constructor(private service: WorkshopSubscriptionService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkshopSubscription> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((workshopSubscription: HttpResponse<WorkshopSubscription>) => workshopSubscription.body));
    }
    return of(new WorkshopSubscription());
  }
}

export const workshopSubscriptionRoute: Routes = [
  {
    path: '',
    component: WorkshopSubscriptionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopSubscription.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WorkshopSubscriptionDetailComponent,
    resolve: {
      workshopSubscription: WorkshopSubscriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopSubscription.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WorkshopSubscriptionUpdateComponent,
    resolve: {
      workshopSubscription: WorkshopSubscriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopSubscription.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WorkshopSubscriptionUpdateComponent,
    resolve: {
      workshopSubscription: WorkshopSubscriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopSubscription.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const workshopSubscriptionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: WorkshopSubscriptionDeletePopupComponent,
    resolve: {
      workshopSubscription: WorkshopSubscriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshopSubscription.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
