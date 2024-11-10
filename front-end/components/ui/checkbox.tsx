import React, { forwardRef } from 'react'

export interface CheckboxProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
}

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(
  ({ className = '', label, ...props }, ref) => {
    return (
      <div className="flex items-center">
        <input
          type="checkbox"
          className={`form-checkbox h-5 w-5 text-blue-600 transition duration-150 ease-in-out ${className}`}
          ref={ref}
          {...props}
        />
        {label && (
          <label htmlFor={props.id} className="ml-2 block text-sm text-gray-900">
            {label}
          </label>
        )}
      </div>
    )
  }
)

Checkbox.displayName = 'Checkbox'