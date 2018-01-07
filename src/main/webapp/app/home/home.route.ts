import {HomeComponent} from './';
import {ProductDetailComponent} from './product-detail.component';
import {Route} from '@angular/router';
import {ProductOrderPopupComponent} from './product-order.component';

export const HOME_ROUTE: Route[] = [{
    path: '', component: HomeComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}, {
    path: 'product/:id', component: ProductDetailComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}, {
    path: 'product/:id/order', component: ProductOrderPopupComponent, data: {
        authorities: ['USER_USER'], pageTitle: 'home.title'
    }, outlet: 'popup'
}];
