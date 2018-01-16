/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {Observable} from 'rxjs/Observable';

import {KransenzoTestModule} from '../../../test.module';
import {OrderDetailComponent} from '../../../../../../main/webapp/app/entities/order/order-detail.component';
import {OrderService} from '../../../../../../main/webapp/app/entities/order/order.service';
import {Order} from '../../../../../../main/webapp/app/entities/order/order.model';

describe( 'Component Tests', () => {

    describe( 'Order Management Detail Component', () => {
        let comp: OrderDetailComponent;
        let fixture: ComponentFixture<OrderDetailComponent>;
        let service: OrderService;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule], declarations: [OrderDetailComponent], providers: [OrderService]
                                            } )
                .overrideTemplate( OrderDetailComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( OrderDetailComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( OrderService );
        } );

        describe( 'OnInit', () => {
            it( 'Should call load all on init', () => {
                // GIVEN

                spyOn( service, 'find' ).and.returnValue( Observable.of( new Order( 123 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.order ).toEqual( jasmine.objectContaining( {id: 123} ) );
            } );
        } );
    } );

} );
