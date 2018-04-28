/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {KransenzoTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
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
                                                imports: [KransenzoTestModule], declarations: [OrderDetailComponent], providers: [JhiDateUtils, JhiDataUtils, DatePipe, {
                    provide: ActivatedRoute, useValue: new MockActivatedRoute( {id: 123} )
                }, OrderService, JhiEventManager]
                                            } ).overrideTemplate( OrderDetailComponent, '' )
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

                spyOn( service, 'find' ).and.returnValue( Observable.of( new Order( 10 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.order ).toEqual( jasmine.objectContaining( {id: 10} ) );
            } );
        } );
    } );

} );
