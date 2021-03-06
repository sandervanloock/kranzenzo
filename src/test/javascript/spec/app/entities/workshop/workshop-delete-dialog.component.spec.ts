/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopDeleteDialogComponent } from 'app/entities/workshop/workshop-delete-dialog.component';
import { WorkshopService } from 'app/entities/workshop/workshop.service';

describe('Component Tests', () => {
    describe('Workshop Management Delete Component', () => {
        let comp: WorkshopDeleteDialogComponent;
        let fixture: ComponentFixture<WorkshopDeleteDialogComponent>;
        let service: WorkshopService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KranzenzoTestModule],
                declarations: [WorkshopDeleteDialogComponent]
            })
                .overrideTemplate(WorkshopDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WorkshopDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkshopService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
