import {HomeComponent} from './';
import {ProductDetailComponent} from './product-detail.component';
import {Route} from '@angular/router';

export const HOME_ROUTE: Route[] = [{
    path: '', component: HomeComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}, {
    path: 'product/:id', component: ProductDetailComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}];
