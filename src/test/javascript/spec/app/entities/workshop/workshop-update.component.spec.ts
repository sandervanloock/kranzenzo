import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopUpdateComponent } from 'app/entities/workshop/workshop-update.component';
import { WorkshopService } from 'app/entities/workshop/workshop.service';
import { Workshop } from 'app/shared/model/workshop.model';

describe('Component Tests', () => {
  describe('Workshop Management Update Component', () => {
    let comp: WorkshopUpdateComponent;
    let fixture: ComponentFixture<WorkshopUpdateComponent>;
    let service: WorkshopService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KranzenzoTestModule],
        declarations: [WorkshopUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WorkshopUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkshopUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkshopService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Workshop(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Workshop();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
