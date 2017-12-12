import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';

import {OrderComponent} from './order.component';
import {OrderDetailComponent} from './order-detail.component';
import {OrderPopupComponent} from './order-dialog.component';
import {OrderDeletePopupComponent} from './order-delete-dialog.component';

export const orderRoute: Routes = [{
    path: 'order', component: OrderComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.order.home.title'
    }, canActivate: [UserRouteAccessService]
}, {
    path: 'order/:id', component: OrderDetailComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.order.home.title'
    }, canActivate: [UserRouteAccessService]
}];

export const orderPopupRoute: Routes = [{
    path: 'order-new', component: OrderPopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.order.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'order/:id/edit', component: OrderPopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.order.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}, {
    path: 'order/:id/delete', component: OrderDeletePopupComponent, data: {
        authorities: ['ROLE_USER'], pageTitle: 'kransenzoApp.order.home.title'
    }, canActivate: [UserRouteAccessService], outlet: 'popup'
}];
