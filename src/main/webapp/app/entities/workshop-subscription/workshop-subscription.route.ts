import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';

import {WorkshopSubscriptionComponent} from './workshop-subscription.component';
import {WorkshopSubscriptionDetailComponent} from './workshop-subscription-detail.component';
import {WorkshopSubscriptionPopupComponent} from './workshop-subscription-dialog.component';
import {WorkshopSubscriptionDeletePopupComponent} from './workshop-subscription-delete-dialog.component';

export const workshopSubscriptionRoute: Routes = [{
    path: 'workshop-subscription', component: WorkshopSubscriptionComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopSubscription.home.title'
    }, canActivate: [UserRouteAccessService]
}, {
    path: 'workshop-subscription/:id', component: WorkshopSubscriptionDetailComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopSubscription.home.title'
    }, canActivate: [UserRouteAccessService]
}];

export const workshopSubscriptionPopupRoute: Routes = [{
    path: 'workshop-subscription-new', component: WorkshopSubscriptionPopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopSubscription.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'workshop-subscription/:id/edit', component: WorkshopSubscriptionPopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopSubscription.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'workshop-subscription/:id/delete', component: WorkshopSubscriptionDeletePopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.workshopSubscription.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}];
