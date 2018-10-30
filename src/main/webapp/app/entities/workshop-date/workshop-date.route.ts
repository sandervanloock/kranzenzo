import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IWorkshopDate, WorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from './workshop-date.service';
import { WorkshopDateComponent } from './workshop-date.component';
import { WorkshopDateDetailComponent } from './workshop-date-detail.component';
import { WorkshopDateUpdateComponent } from './workshop-date-update.component';
import { WorkshopDateDeletePopupComponent } from './workshop-date-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class WorkshopDateResolve implements Resolve<IWorkshopDate> {
    constructor(private service: WorkshopDateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((workshopDate: HttpResponse<WorkshopDate>) => workshopDate.body));
        }
        return of(new WorkshopDate());
    }
}

export const workshopDateRoute: Routes = [
    {
        path: 'workshop-date',
        component: WorkshopDateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kranzenzoApp.workshopDate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'workshop-date/:id/view',
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
        path: 'workshop-date/new',
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
        path: 'workshop-date/:id/edit',
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
        path: 'workshop-date/:id/delete',
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
