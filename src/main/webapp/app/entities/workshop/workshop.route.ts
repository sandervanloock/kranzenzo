import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';

import {WorkshopComponent} from './workshop.component';
import {WorkshopDetailComponent} from './workshop-detail.component';
import {WorkshopPopupComponent} from './workshop-dialog.component';
import {WorkshopDeletePopupComponent} from './workshop-delete-dialog.component';

export const workshopRoute: Routes = [{
    path: 'workshop', component: WorkshopComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshop.home.title'
    }, canActivate: [UserRouteAccessService]
}, {
    path: 'workshop/:id', component: WorkshopDetailComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshop.home.title'
    }, canActivate: [UserRouteAccessService]
}];

export const workshopPopupRoute: Routes = [{
    path: 'workshop-new', component: WorkshopPopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshop.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'workshop/:id/edit', component: WorkshopPopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshop.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'workshop/:id/delete', component: WorkshopDeletePopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshop.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}];
