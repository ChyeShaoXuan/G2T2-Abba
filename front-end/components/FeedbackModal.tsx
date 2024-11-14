import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogFooter, DialogTitle } from "@/components/ui/dialog";
import { Star } from "lucide-react";

interface FeedbackModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (rating: number, comment: string) => void;
}

const FeedbackModal: React.FC<FeedbackModalProps> = ({ isOpen, onClose, onSubmit }) => {
    const [rating, setRating] = useState<number>(0);
    const [comment, setComment] = useState<string>("");

    const handleRatingClick = (star: number) => setRating(star);

    const handleSubmit = () => {
        onSubmit(rating, comment);
        onClose();
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent aria-describedby="feedback-description">
                <DialogTitle>Leave Feedback</DialogTitle>
                <div className="flex gap-2 my-4">
                    {[1, 2, 3, 4, 5].map((star) => (
                        <Star
                            key={star}
                            onClick={() => handleRatingClick(star)}
                            fill={star <= rating ? "yellow" : "none"}
                            className="cursor-pointer"
                        />
                    ))}
                </div>
                <textarea
                    className="w-full p-2 border rounded"
                    placeholder="Leave your feedback here..."
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    id="feedback-description"
                />
                <DialogFooter>
                    <Button onClick={handleSubmit}>Submit</Button>
                    <Button variant="outline" onClick={onClose}>
                        Cancel
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};

export default FeedbackModal;
