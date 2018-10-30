import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IWorkshop, Workshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';
import { WorkshopComponent } from './workshop.component';
import { WorkshopDetailComponent } from './workshop-detail.component';
import { WorkshopUpdateComponent } from './workshop-update.component';
import { WorkshopDeletePopupComponent } from './workshop-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class WorkshopResolve implements Resolve<IWorkshop> {
    constructor(private service: WorkshopService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((workshop: HttpResponse<Workshop>) => workshop.body));
        }
        return of(new Workshop());
    }
}

export const workshopRoute: Routes = [
    {
        path: 'workshop',
        component: WorkshopComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kranzenzoApp.workshop.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'workshop/:id/view',
        component: WorkshopDetailComponent,
        resolve: {
            workshop: WorkshopResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kranzenzoApp.workshop.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'workshop/new',
        component: WorkshopUpdateComponent,
        resolve: {
            workshop: WorkshopResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kranzenzoApp.workshop.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'workshop/:id/edit',
        component: WorkshopUpdateComponent,
        resolve: {
            workshop: WorkshopResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kranzenzoApp.workshop.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const workshopPopupRoute: Routes = [
    {
        path: 'workshop/:id/delete',
        component: WorkshopDeletePopupComponent,
        resolve: {
            workshop: WorkshopResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kranzenzoApp.workshop.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
