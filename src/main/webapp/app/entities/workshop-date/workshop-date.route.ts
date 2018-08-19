import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';

import {WorkshopDateComponent} from './workshop-date.component';
import {WorkshopDateDetailComponent} from './workshop-date-detail.component';
import {WorkshopDatePopupComponent} from './workshop-date-dialog.component';
import {WorkshopDateDeletePopupComponent} from './workshop-date-delete-dialog.component';

export const workshopDateRoute: Routes = [{
    path: 'workshop-date', component: WorkshopDateComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopDate.home.title'
    }, canActivate: [UserRouteAccessService]
}, {
    path: 'workshop-date/:id', component: WorkshopDateDetailComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopDate.home.title'
    }, canActivate: [UserRouteAccessService]
}];

export const workshopDatePopupRoute: Routes = [{
    path: 'workshop-date-new', component: WorkshopDatePopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopDate.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'workshop-date/:id/edit', component: WorkshopDatePopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopDate.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'workshop-date/:id/delete', component: WorkshopDateDeletePopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopDate.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}];
