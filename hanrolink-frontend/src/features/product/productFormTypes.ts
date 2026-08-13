import type { components } from '../../shared/api/schema'

export type StoryFormData = {
  id?: number
  position: number
  productStorySectionTemplateId: number | ''
  imageFile: File | null
  existingImageUrl?: string
  body: string
}

export type ProductStoryTemplate =
  components['schemas']['ProductStorySectionTemplateOptionResponse']

export type StoryFormChanges = Partial<
  Omit<StoryFormData, 'id' | 'position' | 'existingImageUrl'>
>

type SupplierProductCreateRequest =
  components['schemas']['SupplierProductCreateRequest']

export type ProductInformationFormData = Omit<
  SupplierProductCreateRequest,
  | 'mainImageFile'
  | 'monthlySupplyCapacities'
  | 'productStories'
  | 'productCategoryId'
  | 'mainIngredientRegionId'
  | 'expirationType'
  | 'shelfLifeDays'
  | 'storageType'
  | 'desiredRetailPrice'
  | 'unitsPerCase'
  | 'minimumOrderQuantity'
  | 'shippingLeadTimeDays'
> & {
  mainImageFile: File | null
  productCategoryId: number | ''
  mainIngredientRegionId: number | ''
  expirationType: SupplierProductCreateRequest['expirationType'] | ''
  shelfLifeDays: number | ''
  storageType: SupplierProductCreateRequest['storageType'] | ''
  desiredRetailPrice: number | ''
  unitsPerCase: number | ''
  minimumOrderQuantity: number | ''
  shippingLeadTimeDays: number | ''
}

export type SupplierProductFormOptions =
  components['schemas']['SupplierProductFormOptionsResponse']

type MonthlySupplyCapacityRequest =
  components['schemas']['MonthlySupplyCapacityRequest']

export type MonthlySupplyCapacityFormData = Omit<
  MonthlySupplyCapacityRequest,
  'availableQuantity'
> & {
  availableQuantity: number | ''
}

export type ProductFormValues = {
  productInformations: ProductInformationFormData
  stories: StoryFormData[]
  monthlySupplyCapacities: MonthlySupplyCapacityFormData[]
}

export type ProductFormMode = 'create' | 'edit'

export type ProductFormInitialValues = ProductFormValues & {
  existingMainImageUrl?: string
}

export type ProductFormProps = {
  mode: ProductFormMode
  initialValues: ProductFormInitialValues
  onSubmit: (values: ProductFormValues) => void | Promise<void>
  onCancel?: () => void
}
