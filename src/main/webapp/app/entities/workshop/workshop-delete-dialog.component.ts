import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';

@Component({
  selector: 'jhi-workshop-delete-dialog',
  templateUrl: './workshop-delete-dialog.component.html'
})
export class WorkshopDeleteDialogComponent {
  workshop: IWorkshop;

  constructor(protected workshopService: WorkshopService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.workshopService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'workshopListModification',
        content: 'Deleted an workshop'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-workshop-delete-popup',
  template: ''
})
export class WorkshopDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ workshop }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WorkshopDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.workshop = workshop;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/workshop', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/workshop', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
