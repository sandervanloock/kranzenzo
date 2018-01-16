/* tslint:disable max-line-length */
import {async, ComponentFixture, fakeAsync, inject, TestBed, tick} from '@angular/core/testing';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Observable} from 'rxjs/Observable';
import {JhiEventManager} from 'ng-jhipster';

import {KransenzoTestModule} from '../../../test.module';
import {OrderDialogComponent} from '../../../../../../main/webapp/app/entities/order/order-dialog.component';
import {OrderService} from '../../../../../../main/webapp/app/entities/order/order.service';
import {Order} from '../../../../../../main/webapp/app/entities/order/order.model';
import {CustomerService} from '../../../../../../main/webapp/app/entities/customer';
import {LocationService} from '../../../../../../main/webapp/app/entities/location';
import {ProductService} from '../../../../../../main/webapp/app/entities/product';

describe( 'Component Tests', () => {

    describe( 'Order Management Dialog Component', () => {
        let comp: OrderDialogComponent;
        let fixture: ComponentFixture<OrderDialogComponent>;
        let service: OrderService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule],
                                                declarations: [OrderDialogComponent],
                                                providers: [CustomerService, LocationService, ProductService, OrderService]
                                            } )
                .overrideTemplate( OrderDialogComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( OrderDialogComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( OrderService );
            mockEventManager = fixture.debugElement.injector.get( JhiEventManager );
            mockActiveModal = fixture.debugElement.injector.get( NgbActiveModal );
        } );

        describe( 'save', () => {
            it( 'Should call update service on save for existing entity', inject( [], fakeAsync( () => {
                // GIVEN
                const entity = new Order( 123 );
                spyOn( service, 'update' ).and.returnValue( Observable.of( entity ) );
                comp.order = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect( service.update ).toHaveBeenCalledWith( entity );
                expect( comp.isSaving ).toEqual( false );
                expect( mockEventManager.broadcastSpy ).toHaveBeenCalledWith( {name: 'orderListModification', content: 'OK'} );
                expect( mockActiveModal.dismissSpy ).toHaveBeenCalled();
            } ) ) );

            it( 'Should call create service on save for new entity', inject( [], fakeAsync( () => {
                // GIVEN
                const entity = new Order();
                spyOn( service, 'create' ).and.returnValue( Observable.of( entity ) );
                comp.order = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect( service.create ).toHaveBeenCalledWith( entity );
                expect( comp.isSaving ).toEqual( false );
                expect( mockEventManager.broadcastSpy ).toHaveBeenCalledWith( {name: 'orderListModification', content: 'OK'} );
                expect( mockActiveModal.dismissSpy ).toHaveBeenCalled();
            } ) ) );
        } );
    } );

} );
